interface IStepItem {
  number: number;
  desc: string;
  status: string;
  isValid: boolean;
}

interface IProjectInfoById {
  name: string;
  path: string;
  description: string;
  deafultBranch: string;
  createdAt: string;
  lastActivityAt: string;
}

interface IProject {
  projectConfig: IProjectConfig;
  itemList: IItem[];
  nginxConfig: INginxConfig;
}

interface IProjectConfig {
  hostUrl: string;
  repositoryUrl: string;
  projectId: string;
  projectName: string;
  description: string;
  imageUrl: string;
}

interface IItem {
  itemName: string;
  portNumber: string;
  branchName: string;
  secretToken: string;
  targetFolder: string;
  frameworkType: string;
  buildVersion: string;
  javaVersion: string;
}

interface INginxConfig {
  domainUrl: string;
  sslCertificate: string;
  sslCertificateKey: string;
  proxyPathList: IProxyPath[];
}

interface IProxyPath {
  idx: number;
  pathUrl: stirng;
  pathName: stirng;
}
