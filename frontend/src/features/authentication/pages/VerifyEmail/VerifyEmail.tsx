import { useState } from "react";
import { Box } from "../../components/Box/Box";
import { Input } from "../../../../components/input/Input";
import classes from "./VerifyEmail.module.scss";
import { Button } from "../../../../components/Button/Button";
import { useNavigate } from "react-router";

export default function VerifyEmail() {
  const [errorMessage, setErrorMessage] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);

  const validateEmail = async (code: string) => {
    setMessage("");
    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/api/v1/authentication/validate-email-ferification-tokne?=token=${code}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
      );
      if (response.ok) {
        setErrorMessage("");
        navigate("/");
      }
      const { message } = await response.json();
      setErrorMessage(message);
    } catch (e) {
      console.log(e);
      setErrorMessage("Something went wrong please try again");
    } finally {
      setIsLoading(false);
    }
  };

  const sendEmailVerificationToken = async () => {
    setMessage("");
    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/api/v1/authentication/validate-email-ferification-token`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
      );
      if (response.ok) {
        setErrorMessage("");
        setMessage("Code sent successfully. Please check your email");
        return;
      }
      const { message } = await response.json();
      setErrorMessage(message);
    } catch (e) {
      console.log(e);
      setErrorMessage("Something went wrong please try again");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={classes.root}>
      <Box>
        <h1>Verify Email</h1>
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            setIsLoading(true);
            const code = e.currentTarget.code.value;
            await validateEmail(code);
            setIsLoading(false);
          }}
        >
          <p>Only one step to complete the registration. Verify your email</p>

          <Input type="text" label="Verification code" key="code" name="code" />
          {message && <p style={{ color: "green" }}>{message}</p>}
          {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
          <Button type="submit" disabled={isLoading}>
            Validate email
          </Button>
          <Button
            onClick={() => sendEmailVerificationToken()}
            outline
            type="button"
            disabled={isLoading}
          >
            Send again
          </Button>
        </form>
      </Box>
    </div>
  );
}
