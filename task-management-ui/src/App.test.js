import { render, screen } from '@testing-library/react';
import App from './App';

test('renders login screen by default', async () => {
  render(<App />);
  const heading = await screen.findByRole('heading', { name: /welcome back/i });
  expect(heading).toBeInTheDocument();
});
