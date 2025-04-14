import { RouterProvider } from "react-router-dom";
import { router } from "./core/routes/config";

function App() {
  return <RouterProvider 
    router={router} 
    future={{
      v7_startTransition: true
    }}
  />;
}

export default App;
