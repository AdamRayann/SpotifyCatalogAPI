package com.example.catalog;

import com.example.catalog.utils.LRUCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LRUCacheTest {

    LRUCache<String, String> cache;

    @Nested
    @DisplayName("when instantiated with capacity 3")
    class WhenInstantiated {

        @BeforeEach
        void createNewCache() {
            cache = new LRUCache<>(3);
        }

        @Test
        @DisplayName("cache is initially empty")
        void isEmpty() {
            assertEquals(0,cache.size());
        }

        @Test
        @DisplayName("throws NullPointerException when getting a null key")
        void throwsExceptionWhenGettingNullKey() {
            // TODO assert NullPointerException thrown on `cache.get(null)`
            assertThrows(NullPointerException.class,()->cache.get(null));

        }

        @Nested
        @DisplayName("after adding 2 elements")
        class AfterAdding2Elements {

            @BeforeEach
            void addElements() {
                cache.set("k1","v1");
                cache.set("k2","v2");

            }

            @Test
            @DisplayName("cache contains the added elements")
            void containsAddedElements() {
                // TODO assert the added 2 elements are available
                assertEquals("v1",cache.get("k1"));
                assertEquals("v2",cache.get("k2"));


            }
        }

        @Nested
        @DisplayName("after adding 3 elements")
        class AfterAdding3Elements {

            @BeforeEach
            void addElements() {
                cache.set("k1","v1");
                cache.set("k2","v2");
                cache.set("k3","v3");
            }

            @Nested
            @DisplayName("when cleared")
            class WhenCleared {

                // addElements (in AfterAdding3Elements) is executed and then clearCache
                // before EACH test case in WhenCleared


                @BeforeEach
                void clearCache() {
                    // TODO clear the cache after
                    cache.clear();
                }
            }
        }

    }
}
