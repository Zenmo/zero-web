import {defineConfig} from "vite"
import react from "@vitejs/plugin-react"

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react({ jsxImportSource: '@emotion/react' })],
    root: "src",
    // for dev
    server: {
        port: 3000,
    },
    build: {
        rollupOptions: {
            output: {
                entryFileNames: "[name].js",
                assetFileNames: '[name].css',
            },
        },
    },
})
