package io.github.hoangmaihuy

import mill.scalalib.BoundDep

package object missinglink {

  final case class IgnoredPackage(name: String, ignoreSubpackages: Boolean = true) extends PackageFilter {

    override def apply(packageName: String): Boolean =
      packageName != name && (!ignoreSubpackages || !packageName.startsWith(s"$name."))

  }

  private[missinglink] implicit object IgnoredPackages extends PackageFilters[IgnoredPackage] {
    def apply(name: String)(filters: Seq[IgnoredPackage]): Boolean = filters.forall(_.apply(name))
  }

  final case class TargetedPackage(name: String, targetSubpackages: Boolean = true) extends PackageFilter {

    override def apply(packageName: String): Boolean =
      packageName == name || (targetSubpackages && packageName.startsWith(s"$name."))

  }

  private[missinglink] implicit object TargetedPackages extends PackageFilters[TargetedPackage] {

    def apply(name: String)(filters: Seq[TargetedPackage]): Boolean =
      filters.exists(_.apply(name))

  }

  private[missinglink] sealed trait PackageFilter {
    def apply(name: String): Boolean
  }

  private[missinglink] trait PackageFilters[T <: PackageFilter] {
    def apply(name: String)(filters: Seq[T]): Boolean
  }

  final case class DependencyFilter(f: coursier.core.Dependency => Boolean) {
    def check(dep: coursier.core.Dependency): Boolean = f(dep)
  }

  object DependencyFilter {

    def apply(
      organization: String = "",
      name: String = "",
      version: String = ""
    ): DependencyFilter = {
      DependencyFilter(dep =>
        (organization.isEmpty || dep.module.organization.value == organization) &&
          (name.isEmpty || dep.module.name.value == name) &&
          (version.isEmpty || dep.version == version)
      )
    }

  }

}
