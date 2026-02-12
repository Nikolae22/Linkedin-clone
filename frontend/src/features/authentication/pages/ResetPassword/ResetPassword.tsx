import { useNavigate } from "react-router";
import { Box } from "../../components/Box/Box";
import { Button } from "../../../../components/Button/Button";
import { Input } from "../../../../components/input/Input";
import classes from "./ResetPassword.module.scss";
import { useState } from "react";

export default function ResetPassword() {
  const navigate = useNavigate();
  const [emailSend, setEmailSend] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [email, setEmail] = useState("");

  const sendPasswordResetToken = async (email: string) => {
    try {
      const response = await fetch(
        import.meta.env.VITE_API_URL +
          "/api/v1/authentication/send-password-reset-token?email=" +
          email,
        {
          method: "PUT",
        },
      );
      if (response.ok) {
        setErrorMessage("");
        setEmailSend(true);
        return;
      }
      const { message } = await response.json();
      setErrorMessage(message);
    } catch (e) {
      console.log(e);
      setErrorMessage("Something went wrong please try again");
    } finally {
      setIsLoading(true);
    }
  };

  const resetPassword = async (
    email: string,
    code: string,
    password: string,
  ) => {
    try {
      const response = await fetch(
        `${import.meta.env.VITE_API_URL}/api/v1/authentication/reset-password?email=
        ${email}&token=${code}&newPassword=${password}`,
        {
          method: "PUT",
        },
      );
      if (response.ok) {
        setErrorMessage("");
        navigate("/login");
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
        <h1>Reset password</h1>
        {!emailSend ? (
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              setIsLoading(true);
              const email = e.currentTarget.email.value;
              await sendPasswordResetToken(email);
              setEmail(email);
              setIsLoading(false);
            }}
          >
            <p>Enter your email and we will send a verification code</p>
            <Input name="email" type="email" label="Email" />
            <p style={{ color: "red" }}>{errorMessage}</p>
            <Button type="submit">Next</Button>
            <Button type="button" outline onClick={() => navigate("/login")}>
              Back
            </Button>
          </form>
        ) : (
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              setIsLoading(true);
              const code = e.currentTarget.code.value;
              const password = e.currentTarget.password.value;
              await resetPassword(email, code, password);
              setIsLoading(false);
            }}
          >
            <p>Enter the verification code</p>
            <Input
              type="text"
              label="Verifcation code"
              key="code"
              name="code"
            />
            <Input
              label="New Password"
              name="password"
              key="password"
              type="password"
              id="password"
            />
            <p style={{ color: "red" }}>{errorMessage}</p>
            <Button type="submit">Reset password</Button>
            <Button
              type="button"
              outline
              onClick={() => {
                setEmailSend(false);
                setErrorMessage("");
              }}
            >
              Back
            </Button>
          </form>
        )}
      </Box>
    </div>
  );
}
