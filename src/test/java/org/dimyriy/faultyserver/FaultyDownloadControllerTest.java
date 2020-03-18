package org.dimyriy.faultyserver;

import org.assertj.core.api.Assertions;
import org.dimyriy.faultyserver.filesystem.OldFileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.constraints.NotNull;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FaultyDownloadControllerTest {
    @NotNull
    private final MockMvc mockMvc;
    @NotNull
    private final OldFileSystem fs;

    @Autowired
    public FaultyDownloadControllerTest(@NotNull final MockMvc mockMvc, @NotNull final OldFileSystem fs) {
        this.mockMvc = mockMvc;
        this.fs = fs;
    }

    @BeforeEach
    void setUp() {
        fs.clear();
        TestUtil.putContent(fs, "1.txt", "this is file 1");
        TestUtil.putContent(fs, "2.txt", "this is file 2");
    }

    @Test
    void testList() throws Exception {
        mockMvc.perform(get("/oldStorage/files"))
               .andDo(print())
               .andExpect(content().string(containsString("1.txt")))
               .andExpect(content().string(containsString("2.txt")));
    }

    @Test
    void testGetExistingFile() throws Exception {
        mockMvc.perform(get("/oldStorage/files/1.txt"))
               .andDo(print())
               .andExpect(content().string(containsString("this is file 1")));
    }

    @Test
    void testGetNonExistingFile() throws Exception {
        mockMvc.perform(get("/oldStorage/files/3.txt"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteExistingFile() throws Exception {
        mockMvc.perform(delete("/oldStorage/files/1.txt"))
               .andExpect(status().is2xxSuccessful());
        Assertions.assertThat(fs.exists("1.txt")).isFalse();
    }

    @Test
    void testDeleteNonExistingFile() throws Exception {
        mockMvc.perform(delete("/oldStorage/files/3.txt"))
               .andExpect(status().isNotFound());
        Assertions.assertThat(fs.exists("1.txt")).isTrue();
        Assertions.assertThat(fs.exists("2.txt")).isTrue();
    }
}