package com.zenmo.orm.user

/**
 * Indicates this operation is not allowed.
 * This maps to HTTP status code 401 or 403 depending on the context.
 */
class Unauthorized(message: String): Exception(message)
