import activity from './activity/index';
import bbs from './bbs/index';
import member from './member/index';
import welcome from './welcome/index';
const vueFiles = {
	bindPhone: () => import( /* webpackChunkName: "pages/bindPhone" */ "./bindPhone/bindPhone.vue")
};
export default {
	...vueFiles,
  ...activity,
	...bbs,
	...member,
	...welcome
};