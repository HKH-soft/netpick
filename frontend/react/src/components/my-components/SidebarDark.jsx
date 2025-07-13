import React, { useState } from 'react'
import {
  CalendarIcon,
  ChartPieIcon,
  DocumentDuplicateIcon,
  FolderIcon,
  HomeIcon,
  UsersIcon,
  ArrowLeftStartOnRectangleIcon
} from '@heroicons/react/24/outline'
import { useAuth } from '../context/AuthContext'
import { useNavigate } from 'react-router-dom'

const initialNavigation = [
  { name: 'Dashboard', href: '#', icon: HomeIcon, current: true},
  { name: 'Team', href: '#', icon: UsersIcon, current: false },
  { name: 'Projects', href: '#', icon: FolderIcon, current: false },
  { name: 'Calendar', href: '#', icon: CalendarIcon, current: false },
  { name: 'Documents', href: '#', icon: DocumentDuplicateIcon, current: false },
  { name: 'Reports', href: '#', icon: ChartPieIcon, current: false },
]
const teams = [
  { id: 1, name: 'Heroicons', href: '#', initial: 'H', current: false },
  { id: 2, name: 'Tailwind Labs', href: '#', initial: 'T', current: false },
  { id: 3, name: 'Workcation', href: '#', initial: 'W', current: false },
]

function classNames(...classes) {
  return classes.filter(Boolean).join(' ')
}

export default function SidebarDark() {
  const {logout,customer} = useAuth()
  const navigate = useNavigate()
  const [navigation, setNavigation] = useState(initialNavigation)
  const handleClick = (clickedName) => {
    setNavigation(currentNav =>
      currentNav.map(item => ({
        ...item,
        current: item.name === clickedName
      }))
    )
  }

  return (
    <div className="w-80 fixed top-0 left-0 h-screen overflow-y-auto flex grow flex-col gap-y-5 overflow-y-auto bg-zinc-900 px-6">
      <div className="flex h-16 shrink-0 items-center">
        <img
          alt="Your Company"
          src="/images/logo-with-text.svg"
          className="h-8 w-auto"
        />
      </div>
      <nav className="flex flex-1 flex-col">
        <ul role="list" className="flex flex-col flex-1 gap-y-7">
          <li>
            <ul role="list" className="-mx-2 space-y-1">
              {navigation.map((item) => (
                <li key={item.name}>
                  <a
                    href={item.href}
                    onClick={() => handleClick(item.name)}
                    className={classNames(
                      item.current ? 'bg-zinc-800 text-white' : 'text-zinc-400 hover:bg-zinc-800 hover:text-white',
                      'group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold',
                    )}
                  >
                    <item.icon aria-hidden="true" className="size-6 shrink-0" />
                    {item.name}
                    {item.count ? (
                      <span
                        aria-hidden="true"
                        className="ml-auto w-9 min-w-max rounded-full bg-zinc-900 px-2.5 py-0.5 text-center text-xs/5 font-medium whitespace-nowrap text-white ring-1 ring-zinc-700 ring-inset"
                      >
                        {item.count}
                      </span>
                    ) : null}
                  </a>
                </li>
              ))}
            </ul>
          </li>
          <li>
            <div className="text-xs/6 font-semibold text-zinc-400">Your teams</div>
            <ul role="list" className="-mx-2 mt-2 space-y-1">
              {teams.map((team) => (
                <li key={team.name}>
                  <a
                    href={team.href}
                    className={classNames(
                      team.current ? 'bg-zinc-800 text-white' : 'text-zinc-400 hover:bg-zinc-800 hover:text-white',
                      'group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold',
                    )}
                  >
                    <span className="flex size-6 shrink-0 items-center justify-center rounded-lg border border-zinc-700 bg-zinc-800 text-[0.625rem] font-medium text-zinc-400 group-hover:text-white">
                      {team.initial}
                    </span>
                    <span className="truncate">{team.name}</span>
                  </a>
                </li>
              ))}
            </ul>
          </li>
          <li className="-mx-6 mt-auto">
              <a
                href=''
                onClick={() => {logout(); navigate("/")}}
                className='text-zinc-400 hover:bg-zinc-800 hover:text-white group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold mx-4'
              >
                <ArrowLeftStartOnRectangleIcon aria-hidden="true" className="size-6 shrink-0" />
                Sign Out
              </a>
            <a
              href="#"
              className="flex items-center gap-x-4 px-6 py-3 text-sm/6 font-semibold text-white hover:bg-zinc-800"
            >
              <img
                alt=""
                src={customer?.gender == false? `https://randomuser.me/api/portraits/women/${customer?.id - 1}.jpg` : `https://randomuser.me/api/portraits/men/${customer?.id - 1}.jpg`}
                className="size-8 rounded-full bg-zinc-800"
              />
              <span className="sr-only">Your profile</span>
              <span className="flex flex-col w-full"> 
                <span aria-hidden="true">{customer?.username}</span>
                {/* <span className="flex flex-row w-full"> */}
                  {/* <span aria-hidden="true" className='text-sm font-medium text-zinc-400'>{customer != null ? customer.email.length > 17 ? customer.email.substring(0,17) + "..." : customer.email :""}</span> */}
                  <span aria-hidden="true" className='text-sm font-medium text-zinc-400 ml-auto'>{customer?.roles[0] == "ROLE_USER" ? "User" : ""}</span>
                {/* </span> */}
              </span>
            </a>
          </li>
        </ul>
      </nav>
    </div>
  )
}
