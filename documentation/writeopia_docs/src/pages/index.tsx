
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import Heading from '@theme/Heading';
import styles from './index.module.css';
import React from 'react';

function HomepageHeader() {
  const {siteConfig} = useDocusaurusContext();
  return (
    <div className={styles.appHeroBanner}>
      <div className="container">
        <div className={styles.app}>
          <Heading as="h1" className="hero__title">
            Application
          </Heading>
          <div className={styles.buttons}>
            <Link
              className="button button--secondary button--lg"
              to="/docs/overview-app">
              Documentation
            </Link>
          </div>
        </div>
      </div>

      <div className="container">
        <div className={styles.sdk}>
          <Heading as="h1" className="hero__title">
            SDK (Developers)
          </Heading>
          <div className={styles.buttons}>
            <Link
              className="button button--secondary button--lg"
              to="/docs/overview-app">
              Documentation
            </Link>
          </div>
          </div>
      </div>
    </div>
    
  );
}

export default function Home(): JSX.Element {
  const {siteConfig} = useDocusaurusContext();
  return (
    <Layout
      title={`Text Editor for Apps`}
      description="Text Editor for Apps">
      <HomepageHeader />
      <main>
        {/* <HomepageFeatures /> */}
      </main>
    </Layout>
  );
}
