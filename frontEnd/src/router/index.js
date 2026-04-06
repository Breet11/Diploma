import { createRouter, createWebHistory } from 'vue-router';
import { isAdmin, isAuthenticated } from '../utils/auth';

const HomeView = () => import('../views/HomeView.vue');
const CarsView = () => import('../views/CarsView.vue');
const LoginView = () => import('../views/LoginView.vue');
const RegisterView = () => import('../views/RegisterView.vue');
const ProfileView = () => import('../views/ProfileView.vue');
const AdminView = () => import('../views/AdminView.vue');

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/cars',
      name: 'cars',
      component: CarsView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
      meta: { requiresAuth: true, requiresAdmin: true }
    }
  ]
});

router.beforeEach((to) => {
  if ((to.name === 'login' || to.name === 'register') && isAuthenticated()) {
    return { name: 'profile' };
  }

  if (to.meta.requiresAuth && !isAuthenticated()) {
    return { name: 'login' };
  }

  if (to.meta.requiresAdmin && !isAdmin()) {
    return { name: 'cars' };
  }

  return true;
});

export default router;
