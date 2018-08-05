package com.lealdidier.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@DisplayName("Cacheable test")
public class CacheableTest {

    @DisplayName("Cache miss")
    @ParameterizedTest
    @CsvSource({"1", "11"})
    public void cacheMiss(String value) throws IOException {
        Function<String, Long> parser = mock(Function.class);
        when(parser.apply(value)).thenReturn(Long.parseLong(value));
        Cacheable<String, Long> c = new Cacheable<>("StringToLongCacheMiss", String.class, Long.class,
                parser, 2, Duration.ofSeconds(2));
        assertEquals((Long)Long.parseLong(value), c.apply(value));
        verify(parser, times(1)).apply(value);
    }

    @DisplayName("Cache hit")
    @ParameterizedTest
    @CsvSource({"2", "22"})
    public void cacheHit(String value) throws IOException {
        Function<String, Long> parser = mock(Function.class);
        when(parser.apply(value)).thenReturn(Long.parseLong(value));
        Cacheable<String, Long> c = new Cacheable<>("StringToLongCacheHit", String.class, Long.class,
                parser, 2, Duration.ofSeconds(20));
        assertEquals((Long)Long.parseLong(value), c.apply(value));
        assertEquals((Long)Long.parseLong(value), c.apply(value));
        verify(parser, times(1)).apply(value);
    }

    @DisplayName("Cache miss after time to live")
    @ParameterizedTest
    @CsvSource({"3", "33"})
    public void cacheMissAfterTTL(String value) throws IOException, InterruptedException {
        Function<String, Long> parser = mock(Function.class);
        when(parser.apply(value)).thenReturn(Long.parseLong(value));
        Cacheable<String, Long> c = new Cacheable<>("StringToLongCacheMissAfterTTL", String.class, Long.class,
                parser, 2, Duration.ofMillis(1000));
        assertEquals((Long)Long.parseLong(value), c.apply(value));
        Thread.sleep(500);
        assertEquals((Long)Long.parseLong(value), c.apply(value));
        Thread.sleep(1000);
        assertEquals((Long)Long.parseLong(value), c.apply(value));
        verify(parser, times(2)).apply(value);
    }
}
