import {defineConfig} from "vite"
import react from "@vitejs/plugin-react"

export default defineConfig({
    esbuild: {
        keepNames: true,
    },
    // this is a dev-only option
    optimizeDeps: {
        esbuildOptions: {
            // this is necessary for @js-joda/timezone to work in dev mode
            keepNames: true,
        },
    },
    plugins: [react({ jsxImportSource: '@emotion/react' })],
    // for dev
    server: {
        port: 3000,
    },
    build: {
        minify: false,
        rollupOptions: {
            treeshake: false,
            output: {
                entryFileNames: "[name].js",
                assetFileNames: '[name].css',
            },
        },
    },
})
