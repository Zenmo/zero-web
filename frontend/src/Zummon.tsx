
import {com} from "zero-zummon"
import {FunctionComponent} from "react";

// to verify cross-compile
export const Zummon: FunctionComponent = () => {
    const Cake = com.zenmo.zummon.Cake

    const c = new Cake(12)

    return c.toString()
}
