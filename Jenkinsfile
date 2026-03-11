pipeline {
    agent any

    environment {
        // SET YOUR THRESHOLD HERE (e.g., 90%)
        PASS_THRESHOLD = 90.0
        // Ensure you have a Credential ID set up in Jenkins for Git pushing
        GIT_CREDS = 'Admin'
    }

    options {
        skipStagesAfterUnstable()
    }

    stages {
        stage('Build') {
            steps {
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                // Run tests and allow the pipeline to continue even if tests fail
                // so we can capture the results in the post block
                bat 'mvn test -Dmaven.test.failure.ignore=true'
            }
            post {
                always {
                    script {
                        // 1. Capture results and calculate percentage
                        def testResults = junit 'target/surefire-reports/*.xml'
                        
                        double total = testResults.totalCount
                        double passed = testResults.passCount
                        double percent = (total > 0) ? (passed / total) * 100 : 0
                        
                        // Store in environment to pass to other stages/tools
                        env.ACTUAL_PASS_PERCENT = percent
                        echo "Captured Pass Percentage: ${env.ACTUAL_PASS_PERCENT}%"
                    }
                }
            }
        }

                stage('Quality Gate & Revert') {
            steps {
                script {
                    double actual = env.ACTUAL_PASS_PERCENT.toDouble()
                    double limit = env.PASS_THRESHOLD.toDouble()

                    if (actual < limit) {
                        echo "FAILED: Pass rate ${actual}% is below threshold ${limit}%."
                        
                        withCredentials([usernamePassword(credentialsId: "${GIT_CREDS}", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                            
                            // --- ADD THESE CONFIG LINES ---
                            bat 'git config user.email "budchane24@gmail.com"'
                            bat 'git config user.name "bhagyashri"'
                            
                            // Perform revert
                            bat "git revert --no-edit HEAD"
                            
                            // --- CHANGE THIS PUSH LINE ---
                            // Replace '://github.com' with your actual repo link
                            bat "git push https://github.com/Bhagyashri099/TestingAuto.git HEAD"
                        }
                        
                        error("Build Reverted due to low test pass rate.")
                    } else {
                        echo "PASSED: Pass rate ${actual}% meets threshold."
                    }
                }
            }
        }


        stage('Deliver') {
            steps {
                // 3. Pass information back to the build tool (Maven)
                bat "mvn help:evaluate -Dexpression=project.version -DtestRate=${env.ACTUAL_PASS_PERCENT}"
                bat 'jenkins\\scripts\\delivery.bat'
            }
        }
    }
}
