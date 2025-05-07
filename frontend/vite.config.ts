import {defineConfig} from "vite"
import react from "@vitejs/plugin-react"
import {execSync} from "child_process"

// Put hash in file name so that the users don't get stale JS from their browser cache.
let browserCacheKey = (process.env.BROWSER_CACHE_KEY || "").trim()
if (!browserCacheKey) {
    browserCacheKey = execSync("git rev-parse HEAD:frontend").toString().trim()
        .slice(0, 4)
}

export default defineConfig({
    plugins: [react({ jsxImportSource: '@emotion/react' })],
    // for dev
    server: {
        port: 3000,
    },
    build: {
        // minify: false,
        rollupOptions: {
            // Tree shaking seems too aggressive on the KotlinJS code
            treeshake: false,
            output: {
                entryFileNames: `[name].${browserCacheKey}.js`,
                assetFileNames: `[name].${browserCacheKey}.css`,
            },
        },
    },
})
