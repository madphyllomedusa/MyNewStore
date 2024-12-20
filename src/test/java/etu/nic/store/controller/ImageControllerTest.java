package etu.nic.store.controller;

import etu.nic.store.config.JwtService;
import etu.nic.store.config.SecurityConfig;
import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ImageController.class)
@Import(SecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provideImageUploadTestData")
    @WithMockUser(roles = "ADMIN")
    void testUploadImage(MockMultipartFile file, boolean success, int expectedStatus) throws Exception {
        if (success) {
            when(cloudinaryService.upload(any())).thenReturn("http://example.com/image.jpg");
        } else {
            when(cloudinaryService.upload(any())).thenThrow(new BadRequestException("File upload failed"));
        }

        if (file == null) {
            mockMvc.perform(multipart("/product/image")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().is(expectedStatus));
        } else {
            mockMvc.perform(multipart("/product/image")
                            .file(file)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().is(expectedStatus));
        }
    }

    private static Stream<Object[]> provideImageUploadTestData() {
        return Stream.of(
                new Object[]{new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[]{1, 2, 3}), true, 201},
                new Object[]{new MockMultipartFile("file", "image.png", "image/png", new byte[]{4, 5, 6}), true, 201},
                new Object[]{new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[]{}), false, 400},
                new Object[]{null, false, 400}
        );
    }
}
