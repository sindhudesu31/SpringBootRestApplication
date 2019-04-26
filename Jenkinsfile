pipeline{
  agent any
  
  tools{
    Maven "maven_3.3.3"
    Java "jdk 1.8"
  }
  options{
    buildDiscarder(logRotator(numToKeepStr: '3'))
    disableConcurrentBuilds()
    retry(2)
  }
  stages {
    stage('Build') {
      steps {
        bat "mvn clean install"
      }
    }
	Stage('deploy'){
		steps{
			bat "cf push -f manifest.yml"
		}
	}
  }
}
