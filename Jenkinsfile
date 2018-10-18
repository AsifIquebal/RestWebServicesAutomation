pipeline {
    agent any
    stages {
        stage('Cleaning Stage') {
            steps {
                withMaven(maven : 'MyMaven'){
                    bat 'mvn clean'
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