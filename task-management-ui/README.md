<div align="center">

# Task Management UI

A React web client for the Task Management System microservices stack. It gives administrators and collaborators a clean workspace to manage tasks, review submissions, and collaborate securely.

</div>

## Overview

- **Role-based dashboard** surfaces personalised task feeds and quick admin tools.
- **Task lifecycle management** lets admins create, assign, and complete work with a single click.
- **Submission tracking** provides a central place to review deliverables and status updates.
- **Responsive UI** built with reusable components, modern accessibility patterns, and mobile-friendly layouts.

## Tech Stack

- **Framework:** React 18 (Create React App)
- **Routing:** React Router v6
- **HTTP:** Axios with request interceptors for JWT auth
- **State Management:** React Context + hooks for auth, local component state elsewhere
- **Styling:** Custom CSS modules with design tokens and utility classes
- **Testing:** React Testing Library + Jest (CRA defaults)

## Getting Started

### 1. Install dependencies

```bash
npm install
```

### 2. Configure environment variables

Create a `.env` file from the provided template:

```bash
cp .env.example .env
```

| Variable | Default | Purpose |
| --- | --- | --- |
| `REACT_APP_API_BASE_URL` | `http://localhost:8090` | Base URL for the API Gateway that proxies requests to auth, user, task, and submission services. |

> The UI reads these variables at build time. Restart the dev server after editing `.env`.

### 3. Run locally

```bash
npm start
```

- Opens http://localhost:3000
- Proxies API calls to `REACT_APP_API_BASE_URL`
- Auto-reloads on code changes

### 4. Run tests

```bash
npm test
```

### 5. Build for production

```bash
npm run build
```

Outputs an optimised bundle to `build/` ready for static hosting.

## Project Structure

```
task-management-ui/
├── public/               # Static assets served by CRA
├── src/
│   ├── components/
│   │   ├── Admin/        # Admin-specific panels
│   │   ├── Auth/         # Login & registration screens
│   │   ├── Layout/       # App-wide layout primitives (AppLayout, Navbar, Dashboard)
│   │   ├── Submission/   # Submission list & form
│   │   ├── Task/         # Task CRUD and detail components
│   │   └── common/       # Reusable UI primitives (Loader, EmptyState, PageSection)
│   ├── context/          # React Context providers (AuthContext)
│   ├── hooks/            # Reusable hooks (useAsync, etc.)
│   ├── services/         # Axios instance and API helpers
│   ├── styles/           # Global theme variables and layout styles
│   ├── utils/            # Formatting helpers
│   ├── App.js            # Route definitions with protected/public guards
│   ├── index.js          # Entry point
│   └── ...
├── .env.example          # Environment variable template
├── package.json
└── README.md
```

## Common Commands

- `npm start` — Launch dev server (port 3000)
- `npm test` — Run Jest + React Testing Library suite
- `npm run build` — Create production build
- `npm run eject` — Expose CRA build config (irreversible; avoid unless necessary)

## Error Fix Summary

- **Dependency alignment:** Downgraded React 19 beta packages to stable React 18 + React Router 6 to restore compatibility with `react-scripts`.
- **API resilience:** Centralised Axios base URL via env vars, normalised paths, and hardened interceptor behaviour.
- **Authentication flow:** Refactored context to securely persist JWTs, lazily hydrate the user profile, and gate routes with dedicated public/protected wrappers.
- **UI overhaul:** Replaced inline styles with design tokens, created reusable layout primitives, introduced loaders/empty states, and improved form accessibility (labels, helper text, aria attributes).
- **Admin toolkit:** Streamlined admin panel loading, error handling, and quick task creation workflow.
- **Testing:** Updated default CRA test to target the login view rendered by the new routing guard.

## Troubleshooting

- **API requests return 401/403:** Confirm `REACT_APP_API_BASE_URL` points to the running API gateway and that the JWT returned from `/auth/signin` is valid.
- **CORS errors in development:** Ensure the gateway includes `http://localhost:3000` in its allowed origins or run the UI behind the same domain via reverse proxy.
- **Blank screen after login:** Inspect browser dev tools for network failures. Missing profile endpoint `/api/users/profile` responses will sign the user out to prevent corrupted sessions.
- **npm audit warnings:** `react-scripts` and transitive dependencies may trigger advisories. Mitigate upstream or upgrade to Vite/CRA alternatives if security policies require zero advisories.
- **Styles not updating:** CRA caches `.env` and CSS. Restart `npm start` after editing environment variables or theme tokens.

## Contributing

Please format code with the included ESLint/Prettier rules (CRA defaults) and keep UI primitives in `components/common` to encourage reuse. Submit PRs with screenshots for notable UI changes.
