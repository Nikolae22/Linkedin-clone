import { Link, useNavigate } from "react-router";
import { Box } from "../../components/Box/Box";
import { Button } from "../../../../components/Button/Button";
import { Input } from "../../../../components/input/Input";
import { Separator } from "../../components/Separator/Separator";
import classes from "./Signup.module.scss";
import { useState, type SubmitEvent } from "react";
import { useAuthentication } from "../../contexts/AuthenticationContextProvider";

export default function Signup() {
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const { signup } = useAuthentication();
  const navigate = useNavigate();

  const doSignup = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsLoading(true);
    const email = e.currentTarget.email.value;
    const password = e.currentTarget.password.value;
    try {
      await signup(email, password);
      navigate("/");
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage("An unknown error occurred.");
      }
    } finally {
      setIsLoading(false);
    }
  };
  return (
    <div className={classes.root}>
      <Box>
        <h1>Signup</h1>
        <p>Make the most of your usless life</p>
        <form onSubmit={doSignup}>
          <Input type="email" id="email" label="Email" />
          <Input type="password" id="password" label="Password" />
          {errorMessage && <p className={classes.error}>{errorMessage}</p>}
          <p className={classes.disclaimer}>By click here Agree & Join</p>
          <Button type="submit" disabled={isLoading}>
            Agrre & Join
          </Button>
        </form>
        <Separator>Or</Separator>
        <div className={classes.register}>
          Aready On?
          <Link to="/authentication/login">Sign in</Link>
        </div>
      </Box>
    </div>
  );
}
