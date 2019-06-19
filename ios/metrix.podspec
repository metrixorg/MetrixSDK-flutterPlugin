#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'metrix'
  s.version          = '0.9.2'
  s.summary          = 'Metrix Flutter/ios plugin.'
  s.description      = <<-DESC
Metrix Flutter/ios plugin.
                       DESC
  s.homepage         = 'https://metrix.ir'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'metrixir' => 'admin@ metrix.ir' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.preserve_paths = 'MetrixSdk.framework'
  s.xcconfig = { 'OTHER_LDFLAGS' => '-framework MetrixSdk' }
  s.vendored_frameworks = 'MetrixSdk.framework'

  s.ios.deployment_target = '8.0'
end

