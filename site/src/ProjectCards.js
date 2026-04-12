import { useState } from "react";

const projects = [
  {
    tag: "Passion Project",
    title: "Chess",
    desc: 'As a passion project, I have been building "Frost", a chess AI, using minimax strategies.',
    link: "#",
  },
  {
    tag: "School Project",
    title: "PacMan",
    desc: "This version of an AI that can play the game of PacMan is based off of the CS 188 from the University of Berkeley.",
    link: "#",
  },
  {
    tag: "Internship",
    title: "Primes",
    desc: "This is a paper on Primes in different domains, the topic of research I helped conduct under Dr. Gasarch at the University of Maryland, College Park.",
    link: "https://arxiv.org/abs/2510.15255v1",
  },
  {
    tag: "Passion Project",
    title: "Super Tic Tac Toe",
    desc: "One of the first games I made a program to play, a more difficult version of Tic Tac Toe, with some interesting strategies.",
    link: "SuperTicTacToe.html",
  }
];

const styles = {
  section: {
    padding: "7rem 3rem",
    borderBottom: "1px solid rgba(26,24,20,0.12)",
    background: "#282c34",
  },
  sectionHeader: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "baseline",
    marginBottom: "4rem",
  },
  sectionLabel: {
    fontSize: "0.75rem",
    letterSpacing: "0.12em",
    textTransform: "uppercase",
    color: "#C8472B",
  },
  sectionCount: {
    fontSize: "0.8rem",
    color: "#6B6760",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(2, 1fr)",
    gap: "2px",
  },
  card: {
    background: "#EFECE6",
    padding: "2.5rem",
    cursor: "pointer",
    position: "relative",
    transition: "background 0.2s",
    textDecoration: "none",
    display: "block",
    color: "#1A1814",
    fontFamily: "'DM Sans', sans-serif",
  },
  cardHover: {
    background: "#E5E1D9",
  },
  tag: {
    fontSize: "0.72rem",
    letterSpacing: "0.1em",
    textTransform: "uppercase",
    color: "#C8472B",
    marginBottom: "1.25rem",
  },
  title: {
    fontFamily: "'DM Serif Display', serif",
    fontSize: "1.8rem",
    letterSpacing: "-0.01em",
    marginBottom: "0.75rem",
    lineHeight: 1.2,
  },
  desc: {
    color: "#6B6760",
    fontSize: "0.9rem",
    lineHeight: 1.7,
  },
  arrow: {
    position: "absolute",
    top: "2.5rem",
    right: "2.5rem",
    fontSize: "1.2rem",
    color: "#1A1814",
    transition: "opacity 0.2s, transform 0.2s",
  },
};

function ProjectCard({ tag, title, desc, link }) {
  const [hovered, setHovered] = useState(false);

  return (
    <a
      href={link}
      style={{ ...styles.card, ...(hovered ? styles.cardHover : {}) }}
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
    >
      <span
        style={{
          ...styles.arrow,
          opacity: hovered ? 1 : 0,
          transform: hovered ? "translate(0,0)" : "translate(-4px, 4px)",
        }}
      >
        ↗
      </span>
      <div style={styles.tag}>{tag}</div>
      <div style={styles.title}>{title}</div>
      <p style={styles.desc}>{desc}</p>
    </a>
  );
}

export default function ProjectCards() {
  return (
    <section style={styles.section} id="work">
      <div style={styles.sectionHeader}>
        <div style={styles.sectionLabel}>Selected work</div>
        <div style={styles.sectionCount}>{projects.length.toString().padStart(2, "0")} projects</div>
      </div>
      <div style={styles.grid}>
        {projects.map((p) => (
          <ProjectCard key={p.title} {...p} />
        ))}
      </div>
    </section>
  );
}