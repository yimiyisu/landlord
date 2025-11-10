import components from "./components/index";
import Layout from "./layout/index.vue";
import dict from "./pages/dict";
import Routes from "./pages/routes";
import "./styles/flex.css";
__webpack_public_path__ = zen.path("landlord");

zen.setup({ com: Layout, routes: Routes, dict, components });
