package etu.nic.store.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import etu.nic.store.exceptionhandler.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CloudinaryServiceImplTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForUpload")
    void testUpload(boolean isFileEmpty, boolean cloudinaryThrowsException, boolean shouldThrowException, String expectedUrl) throws IOException {
        when(file.isEmpty()).thenReturn(isFileEmpty);
        when(file.getOriginalFilename()).thenReturn("test.png");

        if (!isFileEmpty) {
            when(file.getBytes()).thenReturn("test data".getBytes());
        }

        if (!isFileEmpty && !cloudinaryThrowsException) {
            Map<String, String> uploadResult = new HashMap<>();
            uploadResult.put("url", expectedUrl);
            when(uploader.upload(any(), any())).thenReturn(uploadResult);
        } else if (cloudinaryThrowsException) {
            when(uploader.upload(any(), any())).thenThrow(new IOException("Cloudinary error"));
        }

        if (shouldThrowException) {
            BadRequestException exception = assertThrows(BadRequestException.class, () -> cloudinaryService.upload(file));
            if (isFileEmpty) {
                assertEquals("Файл отсутствует", exception.getMessage());
            } else if (cloudinaryThrowsException) {
                assertTrue(exception.getMessage().contains("Failed to upload file to Cloudinary"));
            }
        } else {
            String result = cloudinaryService.upload(file);
            assertEquals(expectedUrl, result);
        }

        verify(file, times(1)).isEmpty();
        if (!isFileEmpty) {
            verify(file, times(1)).getBytes();
        }
        if (!isFileEmpty && !cloudinaryThrowsException) {
            verify(uploader, times(1)).upload(any(), any());
        } else {
            verify(uploader, times(cloudinaryThrowsException ? 1 : 0)).upload(any(), any());
        }
    }

    static Stream<Object[]> provideTestCasesForUpload() {
        return Stream.of(
                new Object[]{false, false, false, "http://cloudinary.com/test.png"},
                new Object[]{true, false, true, null},
                new Object[]{false, true, true, null}
        );
    }
}
