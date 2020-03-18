package org.dimyriy.faultyserver;

import org.dimyriy.faultyserver.filesystem.OldFileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest
@AutoConfigureMockMvc
public class FaultyDownloadControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OldFileSystem oldFileSystem;

    @BeforeEach
    void setUp() {
        oldFileSystem.putContent("1.txt", "this is file 1");
        oldFileSystem.putContent("2.txt", "this is file 2");
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/oldStorage/files"))
               .andDo(print())
               .andExpect(content().string(equalTo("[\"1.txt\",\"2.txt\"]")));
    }

    @Test
    void getFile() throws Exception {
        mockMvc.perform(get("/oldStorage/files/1.txt"))
               .andDo(print())
               .andExpect(content().string(containsString("this is file 1")));
    }
}