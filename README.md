# RepoScope 🔍

A GitHub repository health analyzer that scores repositories across key maintenance dimensions and generates an AI-powered issue roadmap when health is low.

---

## What it does

RepoScope analyzes any public GitHub repository and produces a health score out of 100 across four dimensions:

- **Commit frequency** — how consistently is the repo being worked on
- **PR merge rate** — how efficiently are contributions being accepted
- **Issue response time** — how responsive are the maintainers
- **Contributor activity** — how many people are actively involved

When a repository scores below 50, RepoScope automatically generates an **issue dependency roadmap** using a DAG and topological sort algorithm to recommend which issues to fix first.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React.js, Tailwind CSS, Axios |
| Backend | Spring Boot (Java 21) |
| Database | PostgreSQL |
| Authentication | JWT, Spring Security, BCrypt |
| External API | GitHub REST API |
| Build Tool | Maven |

---

## DSA Engine

The core of RepoScope is a hand-built DSA engine:

- **Sliding window** — calculates commit consistency over 90-day windows split into 10-day blocks
- **Weighted scoring** — combines four metrics with context-aware weights (solo vs team repos treated differently)
- **DAG construction** — parses GitHub issue descriptions for dependency references ("blocked by #N", "depends on #N") and builds a directed acyclic graph
- **Kahn's topological sort** — processes the DAG to produce a valid issue fix order
- **Cycle detection** — automatically flags circular dependencies between issues

---
