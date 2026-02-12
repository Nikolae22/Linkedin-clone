import type { Dispatch, SetStateAction } from "react";
import classes from "./Profile.module.scss";
import { useAuthentication } from "../../../../features/authentication/contexts/AuthenticationContextProvider";
import { Button } from "../../../Button/Button";
import { Link, useNavigate } from "react-router";

interface ProfileProps {
  showProfileMenu: boolean;
  setShowNavigationMenu: Dispatch<SetStateAction<boolean>>;
  setShowProfileMenu: Dispatch<SetStateAction<boolean>>;
}

export function Profile({
  showProfileMenu,
  setShowProfileMenu,
  setShowNavigationMenu,
}: ProfileProps) {
  const { logout, user } = useAuthentication();
  const navigate = useNavigate();

  return (
    <div className={classes.rott}>
      <button
        className={classes.toggle}
        onClick={() => {
          setShowProfileMenu((prev) => !prev);
          if (window.innerWidth <= 1080) {
            setShowNavigationMenu(false);
          }
        }}
      >
        <img
          src={user?.profilePicture || "/avatar.png"}
          alt=""
          className={`${classes.top} ${classes.avatar}`}
        />
        <div className={classes.name}>
          <div>{user?.firstName + " " + user?.lastName?.charAt(0) + "."}</div>
        </div>
      </button>
      {showProfileMenu ? (
        <div className={classes.menu}>
          <div className={classes.content}>
            <img
              src={user?.profilePicture || "/avatar.png"}
              alt=""
              className={`${classes.left} ${classes.avater}`}
            />
            <div className={classes.right}>
              <div className={classes.name}>
                {user?.firstName + " " + user?.lastName}
              </div>
              <div className={classes.title}>
                {user?.position + " at " + user?.company}
              </div>
            </div>
            <Button
              size="small"
              outline
              onClick={() => {
                setShowProfileMenu(false);
                navigate("/profile/"+user);
              }}
            >
              View Profile
            </Button>
            <Link to="/settings" onClick={() => setShowProfileMenu(false)}>
              Setting and Privacy
            </Link>
            <Link
              to="/logout"
              onClick={(e) => {
                e.preventDefault();
                logout();
              }}
            >
              SignOut
            </Link>
          </div>
        </div>
      ) : null}
    </div>
  );
}
