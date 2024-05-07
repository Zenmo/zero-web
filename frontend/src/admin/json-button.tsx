
export const JsonButton = ({surveyId}: {surveyId: string}) => (
    <a href={`${process.env.ZTOR_URL}/company-survey/${surveyId}`} className="p-button" css={{
        textDecoration: 'none',
        whiteSpace: 'nowrap',
    }}>
        {"{}"}
    </a>
)