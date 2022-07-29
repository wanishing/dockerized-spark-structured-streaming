set -e
# clean up
rm -rf ./**/target/

# creating app
sbt -batch '
            ; clean
            ; compile
            ; docker:publishLocal
            '