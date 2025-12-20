package br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.user;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.controller.CRUD.WriteController;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserResponseDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.user.UserUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.User;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.IUser.IUserRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.ControllerUtils.createUri;


@RestController
@RequestMapping("users")
@Tag(name = "User Write", description = "Endpoints for writing users")
public class WriteUserController extends WriteController<User, UserRequestDTO, UserResponseDTO, UserUpdateDTO, Long> {

    private final IUserRequestService iUserRequestService;

    public WriteUserController(IUserRequestService userRequestService, ModelMapper modelMapper, IUserRequestService iUserRequestService) {
        super(userRequestService, modelMapper, User.class, UserResponseDTO.class);
        this.iUserRequestService = iUserRequestService;
    }

    @Operation(summary = "Create user", description = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @Override
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        User savedUser = iUserRequestService.createUser(userRequestDTO);
        return ResponseEntity.created(createUri(savedUser)).body(convertToResponseDto(savedUser));
    }

    @Operation(summary = "Update user", description = "Updates an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @Override
    public ResponseEntity<UserResponseDTO> update(@Parameter(description = "User ID") @PathVariable Long id, @RequestBody @Valid UserUpdateDTO updateDTO) {
        User user = iUserRequestService.update(id, updateDTO);
        return ResponseEntity.ok(convertToResponseDto(user));
    }
}
