import pages from "./index";
import welcome from "./welcome/welcome.vue";
let routes = [
    { path: "", redirect: "/welcome" },
    { path: "/welcome", component: welcome },
];
let isExist;

Object.keys(pages).forEach((pagePath) => {
    isExist = false;
    for (let index = 0; index < routes.length; index++) {
        const route = routes[index];
        if (route.path === "/" + pagePath) {
            isExist = true;
            break;
        }
    }
    if (!isExist) {
        routes.push({ path: "/" + pagePath, component: pages[pagePath] });
    }
});
export default routes;
