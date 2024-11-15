import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import Heading from '@theme/Heading';
import styles from './index.module.css';
import React from 'react';
import { Telescope } from 'lucide-react';


function AppSection() {
  return (
    <div className={styles.app}>
      <Heading as="h1" className="hero__title">
        Application
      </Heading>

      <div>
        <p>Keep your documents where you prefer</p>
      </div>
      

      <p>Present your ideas with a click</p>

      <div className={styles.buttons}>
        <Link
          className="button button--secondary button--lg"
          to="/docs/overview-app">
          Documentation
        </Link>
      </div>
    </div>
  );
}


function SdkSection() {
  return (
    <div className={styles.sdk}>
      <Heading as="h1" className="hero__title">
        SDK (Developers)
      </Heading>

      <p>Focus on What Matters</p>

      <p>Robust and very customizable</p>

      <p>Powered by Kotlin</p>

      <div className={styles.buttons}>
        <Link
          className="button button--secondary button--lg"
          to="/docs/overview">
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
