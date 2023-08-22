/**
 * For a detailed explanation regarding each configuration property, visit:
 * https://jestjs.io/docs/configuration
 */

/** @type {import('jest').Config} */
const config = {
  // An array of file extensions your modules use.
  // Ignore JSX for now because it just complicates things because of CSS and images.
  // See https://stackoverflow.com/questions/46177148/how-to-exclude-css-module-files-from-jest-test-suites
  moduleFileExtensions: [
    "js",
    "mjs",
    "cjs",
    // "jsx",
    "ts",
    // "tsx",
    "json",
    "node"
  ],
  testEnvironment: "jsdom",
};

module.exports = config;
