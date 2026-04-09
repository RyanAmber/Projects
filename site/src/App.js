import logo from './logo.svg';
import './App.css';
import ProjectCards from './ProjectCards';

function App() {
  return (
    <div className="App">
      <header className="about-me">
        About Me
        <img src={logo} className="App-logo" alt="logo" />
      </header>
      <header className="Projects">
        Projects
        <ProjectCards />
      </header>
    </div>
  );
}

export default App;
