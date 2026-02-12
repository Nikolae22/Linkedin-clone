
import { Button } from '../../../../components/Button/Button'
import classes from './RightSidebar.module.scss'

export function RightSidebar(){

    return(
        <div className={classes.root}>
            <h3>Add to your feed</h3>
            <div className={classes.items}>
                <div className={classes.item}>
                    <img src="https://i.pravatir.cc/300" alt=""
                    className={classes.avatart}
                    />
                    <div className={classes.content}>
                        <div className={classes.name}>Name</div>
                        <div className={classes.title}>Title</div>
                        <Button
                        size='medium' outline
                        className={classes.button}
                        >
                        + Follow
                        </Button>
                    </div>
                </div>
                <div className={classes.item}>
                    <img src="" alt=""
                    className={classes.avatar}
                    />
                    <div className={classes.content}>
                        <div className={classes.name}>Name</div>
                        <div className={classes.title}>it guy</div>
                        <Button size='medium' outline className={classes.button}>
                            `+ Follow
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    )
}