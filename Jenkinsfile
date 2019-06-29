pipeline {
    agent any
    stages {
        stage('Cleaning Stage') {
            steps {
                withMaven(maven : 'MyMaven'){
                script{
                    if (System.properties['os.name'].toLowerCase().contains('windows')) {
                        echo "it's Windows"
                        bat 'mvn clean'
                    } else {
                        echo "it's not Windows"
                        sh 'mvn clean'
                    }
                }
            }
        }
    }
        stage('Testing Stage') {
              steps {
                  withMaven(maven : 'MyMaven'){
                      bat 'mvn test'
                  }
              }
        }
    }
    post {
        always {
            echo 'I will always say Hello again!'
        }
    }
}
// Make sure Jenkins has Maven Plugin : https://wiki.jenkins.io/display/JENKINS/Pipeline+Maven+Plugin
