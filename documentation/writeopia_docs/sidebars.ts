import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

const sidebars: SidebarsConfig = {
  applicationSidebar: [
    {
      type: 'doc',
      id: 'overview',
    },  
    {
      type: 'category',
      label: 'Getting started',
      items: ['tutorial-basics/basics', 'tutorial-basics/persistence'],
    },  
    {
      type: 'doc',
      id: 'api_reference',
    },
  ],
};

export default sidebars;
