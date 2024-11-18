import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import styles from './index.module.css';
import React from 'react';

function AppSection() {
  return (
    <div className={styles.app}>
      <h1 className={styles.title}>Application</h1>
    
      <p className={styles.item}>Keep your documents where you prefer</p>      
      <p className={styles.item}>Present your ideas with a click</p>

      <div className={styles.buttons}>
        <Link
          className="button button--secondary button--lg"
          to="/docs/application/overview-app">
          Documentation
        </Link>
      </div>
    </div>
  );
}

function SdkSection() {
  return (
    <div className={styles.sdk}>
      <h1 className={styles.title}>SDK (Developers)</h1>

      <p className={styles.item}>Focus on What Matters</p>
      <p className={styles.item}>Robust and very customizable</p>
      <p className={styles.item}>Powered by Kotlin</p>

      <div className={styles.buttons}>
        <Link
          className="button button--secondary button--lg"
          to="/docs/sdk/overview">
          Documentation
        </Link>
      </div>
    </div>
  );
}

function HomepageHeader() {
  const {siteConfig} = useDocusaurusContext();
  return (
    <div className={styles.sectionsContainer}>
      <AppSection />
      <SdkSection />  
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
