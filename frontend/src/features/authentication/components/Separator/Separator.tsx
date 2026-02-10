import type { ReactNode } from "react";
import classes from './Separator.module.scss'


export function Separator({children}:{children:ReactNode}){
    return(
        <div className={classes.separator}>{children}</div>
    )
}