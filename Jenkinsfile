pipeline {
  agent {
    docker {
      image 'openjdk:17'
    }

  }
  stages {
    stage('build') {
      steps {
        sh 'gradlew build'
      }
    }

  }
}