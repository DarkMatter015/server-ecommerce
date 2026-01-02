package br.edu.utfpr.pb.ecommerce.server_ecommerce;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Payment;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.ProductRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.OrderRequestServiceImpl;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@ActiveProfiles("test")
public class OrderConcurrencyTest {

    @Autowired
    private OrderRequestServiceImpl orderService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TestUtils utils;

    @Test
    public void mustGenerateCompetitionErrorWhenTryingToPurchaseSameProductSimultaneously() throws InterruptedException {
        // 1. ARRANGE (PREPARAÇÃO ISOLADA)
        Product product = utils.createProductWithStock(1);
        User user = utils.createValidUser();
        Payment payment = utils.createPayment();
        OrderRequestDTO request = utils.createOrderRequest(product.getId(), payment.getId());

        // 2. ACT (CONFIGURAÇÃO DO ATAQUE)
        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        // Latch de Início (O Gatilho): Segura as threads até darmos o sinal
        CountDownLatch startGate = new CountDownLatch(1);
        // Latch de Fim: Espera todas terminarem
        CountDownLatch endGate = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger optimismLockExceptionCount = new AtomicInteger(0);
        AtomicInteger otherExceptionCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    startGate.await(); // TODAS as threads param aqui e esperam o sinal

                    orderService.validateAndCreateOrder(request, user);
                    successCount.incrementAndGet();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    if (isOptimisticLockingFailure(e)) {
                        optimismLockExceptionCount.incrementAndGet();
                    } else {
                        // Log apenas se for erro inesperado para não sujar o console com erros esperados
                        System.err.println("Erro inesperado: " + e.getMessage());
                        otherExceptionCount.incrementAndGet();
                    }
                } finally {
                    endGate.countDown();
                }
            });
        }

        // 3. FIRE! (DISPARO SIMULTÂNEO)
        startGate.countDown(); // Solta todas as threads ao mesmo tempo
        endGate.await(5, TimeUnit.SECONDS); // Aguarda conclusão
        executorService.shutdown();

        // 4. ASSERT (VALIDAÇÕES)
        Assertions.assertEquals(1, successCount.get(),
                "Apenas uma thread deveria conseguir realizar a compra.");

        Assertions.assertEquals(numberOfThreads - 1,
                optimismLockExceptionCount.get() + otherExceptionCount.get(),
                "Todas as outras threads deveriam falhar por concorrência.");

        Product finalProduct = productRepository.findById(product.getId()).orElseThrow();
        Assertions.assertEquals(0, finalProduct.getQuantityAvailableInStock(),
                "O estoque final deve ser 0.");
    }

    // --- HELPER METHODS (Privados para limpar o teste) ---

    private boolean isOptimisticLockingFailure(Exception e) {
        return e instanceof ObjectOptimisticLockingFailureException ||
                (e.getCause() instanceof ObjectOptimisticLockingFailureException);
    }
}