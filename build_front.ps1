$path = Get-Location
Set-Location ../../WebstormProjects/mikrotik_parser

npm install
npm run build

if (Test-Path "./dist/") {
    Copy-Item -Path "./dist/*" -Destination "$path/src/main/resources/static" -Recurse -Force
    Set-Location $path
} else {
    Write-Error "Coping error"
    Set-Location $path
    exit 1
}