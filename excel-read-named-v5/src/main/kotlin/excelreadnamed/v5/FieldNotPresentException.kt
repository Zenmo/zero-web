package com.zenmo.excelreadnamed.v5

import java.lang.Exception

class FieldNotPresentException(field: String): Exception("Field $field not present in the document")