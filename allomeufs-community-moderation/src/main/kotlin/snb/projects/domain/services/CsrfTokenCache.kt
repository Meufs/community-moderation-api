package snb.projects.domain.services

import io.quarkus.cache.Cache
import io.quarkus.cache.CacheName
import io.quarkus.cache.CaffeineCache
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.CompletableFuture


@ApplicationScoped
class CsrfTokenCache {

    @CacheName("csrf-token-cache")
    private lateinit var cache: Cache

    fun addItem(id: String, token: String) {
        cache.`as`(CaffeineCache::class.java).put(id, CompletableFuture.completedFuture(token))
    }

    fun getUserToken(key: String): String {
        return cache.`as`(CaffeineCache::class.java).getIfPresent<String>(key).get()
    }
}