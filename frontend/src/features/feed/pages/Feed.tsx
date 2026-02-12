import { useNavigate } from "react-router";
import { LeftSidebar } from "../components/LeftSidebar/LeftSidebar";
import { RightSidebar } from "../components/RightSidebar/RightSidebar";
import classes from "./Feed.module.scss";
import { useAuthentication } from "../../authentication/contexts/AuthenticationContextProvider";
import { Button } from "../../../components/Button/Button";
import { useEffect, useState } from "react";
import { Post } from "../components/Post/Post";

export default function Feed() {
  const [showPostingModal, setShowPostingModal] = useState(false);
  const [feedContent, setFeedContent] = useState<"all" | "connexions">(
    "connexions",
  );
  const [posts, setPosts] = useState<Post[]>([]);
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { user } = useAuthentication();

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await fetch(
          import.meta.env.VITE_API_URL +
            "api/v1/feed" +
            (feedContent === "connexions" ? "" : "/posts"),
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          },
        );
        if (!response.ok) {
          const { message } = await response.json();
          throw new Error(message);
        }
        const data = await response.json();
        setPosts(data);
      } catch (error) {
        if (error instanceof Error) {
          setError(error.message);
        } else {
          setError("An error occured");
        }
      }
    };
    fetchPosts();
  }, [feedContent]);

  return (
    <div className={classes.root}>
      <div className={classes.left}>
        <LeftSidebar />
      </div>
      <div className={classes.center}>
        <div className={classes.posting}>
          <button
            onClick={() => {
              navigate(`/profile${user?.id}`);
            }}
          >
            <img
              className={`${classes.top}${classes.avatar}`}
              src={user?.profilePicture || "/avatar.png"}
              alt=""
            />
          </button>
          <Button outline onClick={() => setShowPostingModal(true)}>
            Start a post
          </Button>
          <Modal
            onSubmit={handlePost}
            showModal={showPostingModal}
            setShowModal={setShowPostingModal}
            />
        </div>
      </div>
      <div className={classes.right}>
        <RightSidebar />
      </div>

      {error && <div className={classes.error}>{error}</div>}

      <div className={classes.header}>
        <button
          className={feedContent === "all" ? classes.active : ""}
          onClick={() => setFeedContent("all")}
        >
          All
        </button>
        <button
          className={feedContent === "connexions" ? classes.active : ""}
          onClick={() => setFeedContent("connexions")}
        >
          Feed
        </button>
      </div>
      <div className={classes.feed}>
        {posts.map((post) => (
          <Post key={post.id} post={posts} setPost={setPosts} />
        ))}
      </div>
    </div>
  );
}
