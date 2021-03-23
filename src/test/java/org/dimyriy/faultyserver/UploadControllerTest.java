package org.dimyriy.faultyserver;

import org.assertj.core.api.Assertions;
import org.dimyriy.faultyserver.filesystem.impl.InMemoryInMemoryFileSystemImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.constraints.NotNull;

import static org.dimyriy.faultyserver.TestUtil.putContent;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UploadControllerTest {
    @NotNull
    private final MockMvc mockMvc;
    @NotNull
    private final InMemoryInMemoryFileSystemImpl fs;

    @Autowired
    public UploadControllerTest(@NotNull final MockMvc mockMvc, @NotNull final InMemoryInMemoryFileSystemImpl fs) {
        this.mockMvc = mockMvc;
        this.fs = fs;
    }

    @BeforeEach
    void setUp() {
        fs.clear();
    }

    @Test
    void testList() throws Exception {
        putContent(fs, "1.txt", "this is file 1");
        mockMvc.perform(get("/storage/files"))
               .andDo(print())
               .andExpect(content().string(containsString("1.txt")));
    }

    @Test
    void testGetExistingFile() throws Exception {
        putContent(fs, "1.txt", "this is file 1");
        mockMvc.perform(get("/storage/files/1.txt"))
               .andDo(print())
               .andExpect(content().string(containsString("this is file 1")));
    }

    @Test
    void testGetNonExistingFile() throws Exception {
        putContent(fs, "1.txt", "this is file 1");
        mockMvc.perform(get("/storage/files/3.txt"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteExistingFile() throws Exception {
        putContent(fs, "1.txt", "this is file 1");
        mockMvc.perform(delete("/storage/files/1.txt"))
               .andExpect(status().is2xxSuccessful());
        Assertions.assertThat(fs.exists("1.txt")).isFalse();
    }

    @Test
    void testDeleteNonExistingFile() throws Exception {
        putContent(fs, "1.txt", "this is file 1");
        mockMvc.perform(delete("/storage/files/3.txt"))
               .andExpect(status().isNotFound());
        Assertions.assertThat(fs.exists("1.txt")).isTrue();
    }

    @Test
    void testPostNonExistingFile() throws Exception {
        Assertions.assertThat(fs.exists("1.txt")).isFalse();
        MockMultipartFile file = new MockMultipartFile("file", "1.txt", "text/plain", "this is file 1".getBytes());
        mockMvc.perform(multipart("/storage/files").file(file))
               .andDo(print())
               .andExpect(status().is2xxSuccessful());
        Assertions.assertThat(fs.exists("1.txt")).isTrue();
        Assertions.assertThat(TestUtil.getContent(fs, "1.txt")).isEqualTo("this is file 1");
    }

    @Test
    void testPostExistingFile() throws Exception {
        putContent(fs, "1.txt", "this is an original file");
        Assertions.assertThat(fs.exists("1.txt")).isTrue();
        MockMultipartFile file = new MockMultipartFile("file", "1.txt", "text/plain", "this is file 1".getBytes());
        mockMvc.perform(multipart("/storage/files").file(file))
               .andDo(print())
               .andExpect(status().isConflict());
        Assertions.assertThat(TestUtil.getContent(fs, "1.txt")).isEqualTo("this is an original file");
    }
}
