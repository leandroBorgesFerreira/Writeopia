import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';
import React from 'react';

type FeatureItem = {
  title: string;
  Svg: React.ComponentType<React.ComponentProps<'svg'>>;
  description: JSX.Element;
};

const FeatureList: FeatureItem[] = [
  {
    title: 'Robust and very customizable',
    Svg: require('@site/static/img/Kodee_Assets_Digital_Kodee-naughty.svg').default,
    description: (
      <>
        Writeopia is designed to be very customizable. You have a lot of freedown to choose how your editor will behave. 
        Many use cases are possible. You can customize to fit what you need. 
      </>
    ),
  },
  {
    title: 'Focus on What Matters',
    Svg: require('@site/static/img/Kodee_Assets_Digital_Kodee-winter.svg').default,
    description: (
      <>  
        Writeopia lets you focus on what makes your app great, without spending a lot of time trying to make a great text edition experience. 
        We already did it for you!
      </>
    ),
  },
  {
    title: 'Powered by Kotlin',
    Svg: require('@site/static/img/Kodee_Assets_Digital_Kodee-in-love.svg').default,
    description: (
      <>
        Extend or customize your tex editor layout by reusing Kotlin. There's no need to change everything, you choose what should be persolanized
        and what should stay the way it is. 
      </>
    ),
  },
];

function Feature({title, Svg, description}: FeatureItem) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures(): JSX.Element {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
