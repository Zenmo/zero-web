## Joshi
`Joshi` basically is  for sharing objects between the data portal frontend and backend which don't need to be shared with AnyLogic.

The `joshi` module provides a simplified representation of company surveys, called `IndexSurvey`, 
which contains fewer properties and is suitable for displaying lists on web pages. This module includes the [IndexSurvey](src/commonMain/kotlin/IndexSurvey.kt) data class and related functionality for handling these surveys.

### Key Features
- **IndexSurvey Data Class**: A lightweight version of the company survey with essential properties.
- **Serialization**: The `IndexSurvey` class is serializable, making it easy to convert to and from JSON.
- **JavaScript Export**: The `IndexSurvey` class is exported to JavaScript, allowing it to be used in web applications.

### Example Usage
See usage in [IndexSurveyRepository](../zorm/src/main/kotlin/com/zenmo/orm/companysurvey/IndexSurveyRepository.kt)