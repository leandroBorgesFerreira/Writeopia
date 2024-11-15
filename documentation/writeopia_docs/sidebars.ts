import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

const sidebars = {
  sdkSidebar: [
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
      type: 'category',
      label: 'Customize Drawing',
      items: ['customize-drawing/customize-drawers', 'customize-drawing/default-drawers', 'customize-drawing/default-types'],
    }, 
    {
      type: 'doc',
      id: 'customize-behaviour/customize-ui-commands',
    },
    {
      type: 'category',
      label: 'Text Commands',
      items: ['commands/default-commands', 'commands/command-samples', 'commands/customize-commands'],
    },
    {
      type: 'category',
      label: 'Export Notes',
      items: ['export/export-json', 'export/export-markdown'],
    },
    {
      type: 'doc',
      id: 'api_reference',
    }
  ],
  
  appSidebar: [
    {
      type: 'doc',
      id: 'overview-app',
    }
  ]
  
};

export default sidebars;
