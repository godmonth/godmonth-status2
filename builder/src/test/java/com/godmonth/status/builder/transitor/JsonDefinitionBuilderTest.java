package com.godmonth.status.builder.transitor;

import com.godmonth.status.builder.domain.SampleStatus;
import com.godmonth.status.builder.domain.SampleTrigger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.function.Function;

/**
 * <p></p >
 *
 * @author shenyue
 */
class JsonDefinitionBuilderTest {
    @Test
    void jsonString() throws IOException {
//        String jsonString = FileUtils.readFileToString(new File("src/test/resources/state-machine-sample.json"), StandardCharsets.UTF_8);
        Function<SampleStatus, Function<SampleTrigger, SampleStatus>> function = JsonDefinitionBuilder.<SampleStatus, SampleTrigger>builder().statusClass(SampleStatus.class).triggerClass(SampleTrigger.class).resource(new FileSystemResource("src/test/resources/state-machine-sample.json")).build();
        System.out.println(function);
        Assertions.assertTrue(function.apply(SampleStatus.CREATED) != null);
        Assertions.assertTrue(function.apply(SampleStatus.CREATED).apply(SampleTrigger.PAY) != null);
        Assertions.assertEquals(function.apply(SampleStatus.CREATED).apply(SampleTrigger.PAY), SampleStatus.PAID);
    }

    @Test
    void genericType() throws IOException {
        Function<SampleStatus, Function<SampleTrigger, SampleStatus>> function = JsonDefinitionBuilder.<SampleStatus, SampleTrigger>builder().resource(new FileSystemResource("src/test/resources/state-machine-sample.json")).statusClass(SampleStatus.class).triggerClass(SampleTrigger.class).build();
        System.out.println(function);
        Assertions.assertTrue(function.apply(SampleStatus.CREATED) != null);
        Assertions.assertTrue(function.apply(SampleStatus.CREATED).apply(SampleTrigger.PAY) != null);
        Assertions.assertEquals(function.apply(SampleStatus.CREATED).apply(SampleTrigger.PAY), SampleStatus.PAID);
    }

    @Test
    void stringType() throws IOException {
        Function<String, Function<String, String>> function = JsonDefinitionBuilder.<String, String>builder().resource(new FileSystemResource("src/test/resources/state-machine-sample.json")).statusClass(String.class).triggerClass(String.class).build();
        System.out.println(function);
        Assertions.assertTrue(function.apply("CREATED") != null);
        Assertions.assertTrue(function.apply("CREATED").apply("PAY") != null);
        Assertions.assertEquals(function.apply("CREATED").apply("PAY"), "PAID");
    }
}