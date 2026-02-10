import type { ButtonHTMLAttributes } from "react";
import classes from "./Button.module.scss";

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  outline?: boolean;
};

export function Button({ outline, children, ...others }: ButtonProps) {
  return (
    <Button
      {...others}
      className={`${classes.root} ${outline ? classes.outline : ""}`}
    >
      {children}
    </Button>
  );
}
