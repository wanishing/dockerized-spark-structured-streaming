set -e

# creating app
sbt -batch '
            ; clean
            ; compile
            ; Docker / publishLocal
            '