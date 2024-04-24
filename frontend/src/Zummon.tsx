
import {com} from "zero-zummon"
import {FunctionComponent} from "react";

// to verify cross-compile
export const Zummon: FunctionComponent = () => {
    const surveyJson = JSON.stringify({
        zenmoProject: "Hessie",
        companyName: "Bakkerij Piet",
        personName: "Piet",
        email: "erik@evanv.nl",
        dataSharingAgreed: true,
        addresses: [],
    })
    const survey = com.zenmo.zummon.companysurvey.surveyFromJson(surveyJson);
    console.log(survey);

    return <div>asdf</div>
}
